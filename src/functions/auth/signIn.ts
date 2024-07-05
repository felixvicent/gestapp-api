import type { APIGatewayProxyEventV2 } from "aws-lambda";
import {
  InitiateAuthCommand,
  NotAuthorizedException,
  UserNotConfirmedException,
  UserNotFoundException,
} from "@aws-sdk/client-cognito-identity-provider";

import { response } from "../../utils/response";
import { bodyParser } from "../../utils/bodyParser";
import z, { ZodError } from "zod";
import { cognitoClient } from "../../libs/cognitoClient";

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(6),
});

export async function handler(event: APIGatewayProxyEventV2) {
  try {
    const body = bodyParser(event.body);
    const { email, password } = schema.parse(body);

    const command = new InitiateAuthCommand({
      ClientId: process.env.COGNITO_CLIENT_ID,
      AuthFlow: "USER_PASSWORD_AUTH",
      AuthParameters: {
        USERNAME: email,
        PASSWORD: password,
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

    if (
      error instanceof UserNotFoundException ||
      error instanceof NotAuthorizedException
    ) {
      return response(401, { error: "Invalid credentials" });
    }

    if (error instanceof UserNotConfirmedException) {
      return response(401, {
        error: "You need to confirm your account before sign in",
      });
    }

    return response(500, { error: "Internal Server Error" });
  }
}
