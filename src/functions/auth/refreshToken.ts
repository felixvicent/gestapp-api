import type { APIGatewayProxyEventV2 } from "aws-lambda";
import { InitiateAuthCommand } from "@aws-sdk/client-cognito-identity-provider";

import { response } from "../../utils/response";
import { bodyParser } from "../../utils/bodyParser";
import z, { ZodError } from "zod";
import { cognitoClient } from "../../libs/cognitoClient";

const schema = z.object({
  refreshToken: z.string().min(1),
});

export async function handler(event: APIGatewayProxyEventV2) {
  try {
    const body = bodyParser(event.body);
    const { refreshToken } = schema.parse(body);

    const command = new InitiateAuthCommand({
      ClientId: process.env.COGNITO_CLIENT_ID,
      AuthFlow: "REFRESH_TOKEN_AUTH",
      AuthParameters: {
        REFRESH_TOKEN: refreshToken,
      },
    });

    const { AuthenticationResult } = await cognitoClient.send(command);

    if (!AuthenticationResult) {
      return response(401, { error: "Invalid credentials" });
    }

    return response(201, {
      accessToken: AuthenticationResult.AccessToken,
      refreshToken: AuthenticationResult.RefreshToken,
    });
  } catch (error) {
    if (error instanceof ZodError) {
      return response(400, error);
    }

    return response(500, { error: "Internal Server Error" });
  }
}
