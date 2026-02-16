// Datos para actualizar el perfil del usuario autenticado.
export interface UpdateUserRequest {
  username: string;
  email: string;
  password?: string;
}