//Respuesta que representa la relación entre un usuario y un evento (ej. si le gusta).
export interface UserEventResponse {
  id: number;
  userId: number;
  eventId: number;
  liked: boolean;
  createdAt: string;
}

//Respuesta que contiene el contador total de "me gusta" de un evento.
export interface LikeCountResponse {
  likes: number;
}
