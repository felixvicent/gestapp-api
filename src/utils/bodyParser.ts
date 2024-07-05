export function bodyParser(body: string | undefined) {
  let parsedBody = {};

  try {
    if (body) {
      parsedBody = JSON.parse(body);
    }
  } catch {}

  return parsedBody;
}
