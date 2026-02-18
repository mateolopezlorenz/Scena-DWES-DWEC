// Respuesta del inicio de sesión, contiene el token JWT e información del usuario.
// Respuesta del inicio de sesión, contiene el token JWT e información del usuario.
export interface LoginResponse {
  message: string;
  token: string;
  username: string;
  email: string;
  id: number;
}