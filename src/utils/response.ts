export function response(statusCode: number, data: Record<string, any>) {
  return {
    statusCode,
    body: JSON.stringify(data),
  };
}
