import type { APIGatewayProxyEventV2 } from "aws-lambda";
import {
  SignUpCommand,
  UsernameExistsException,
} from "@aws-sdk/client-cognito-identity-provider";

import { response } from "../../utils/response";
import { bodyParser } from "../../utils/bodyParser";
import z, { ZodError } from "zod";
import { cognitoClient } from "../../libs/cognitoClient";

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(6),
  firstName: z.string().min(1),
  lastName: z.string().min(1),
});

export async function handler(event: APIGatewayProxyEventV2) {
  try {
    const body = bodyParser(event.body);
    const { email, firstName, lastName, password } = schema.parse(body);

    const command = new SignUpCommand({
      ClientId: process.env.COGNITO_CLIENT_ID,
      Username: email,
      Password: password,
      UserAttributes: [
        { Name: "given_name", Value: firstName },
        { Name: "family_name", Value: lastName },
      ],
    });

    const { UserSub } = await cognitoClient.send(command);

    return response(201, { userId: UserSub });
  } catch (error) {
    console.log(error);
    if (error instanceof ZodError) {
      return response(400, error);
    }
    if (error instanceof UsernameExistsException) {
      return response(409, { error: "Username already in use" });
    }
    return response(500, { error: "Internal Server Error" });
  }
}
