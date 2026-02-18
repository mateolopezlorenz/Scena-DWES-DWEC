
// Estructura estándar de una respuesta con token JWT.
export interface JwtResponse {
  token: string;
  type: string;
  name: string;
  email: string;
}