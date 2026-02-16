//Datos necesarios para iniciar sesión.
export interface LoginRequest {
  username: string;
  password: string;
}

// Respuesta del inicio de sesión, contiene el token JWT e información del usuario.
export interface LoginResponse {
  message: string;
  token: string;
  username: string;
  email: string;
  id: number;
}

//Datos necesarios para registrar un nuevo usuario.
export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

// Datos para actualizar el perfil del usuario autenticado.
export interface UpdateUserRequest {
  username: string;
  email: string;
  password?: string;
}

// Estructura estándar de una respuesta con token JWT.
export interface JwtResponse {
  token: string;
  type: string;
  username: string;
  email: string;
}

// Respuesta genérica para mensajes informativos del servidor.
export interface MessageResponse {
  message: string;
}
