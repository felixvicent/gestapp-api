import type { APIGatewayProxyEventV2 } from "aws-lambda";
import {
  CodeMismatchException,
  ConfirmSignUpCommand,
  ExpiredCodeException,
} from "@aws-sdk/client-cognito-identity-provider";

import { response } from "../../utils/response";
import { bodyParser } from "../../utils/bodyParser";
import z, { ZodError } from "zod";
import { cognitoClient } from "../../libs/cognitoClient";

const schema = z.object({
  email: z.string().email(),
  code: z.string().length(6),
});

export async function handler(event: APIGatewayProxyEventV2) {
  try {
    const body = bodyParser(event.body);
    const { email, code } = schema.parse(body);

    const command = new ConfirmSignUpCommand({
      ClientId: process.env.COGNITO_CLIENT_ID,
      Username: email,
      ConfirmationCode: code,
    });

    await cognitoClient.send(command);

    return response(204);
  } catch (error) {
    if (error instanceof ZodError) {
      return response(400, error);
    }

    if (
      error instanceof CodeMismatchException ||
      error instanceof ExpiredCodeException
    ) {
      return response(400, { error: "Invalid or expired code" });
    }

    return response(500, { error: "Internal Server Error" });
  }
}
