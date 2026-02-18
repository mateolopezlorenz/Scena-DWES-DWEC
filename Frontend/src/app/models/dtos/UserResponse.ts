import { Local } from '../localModel';

// Respuesta con información básica de un usuario.
export interface UserResponse {
  id: number;
  name: string;
  email: string;
}