export function response(statusCode: number, data?: Record<string, any>) {
  return {
    statusCode,
    body: data ? JSON.stringify(data) : undefined,
  };
}
