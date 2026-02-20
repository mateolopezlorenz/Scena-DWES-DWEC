//Datos necesarios para actualizar un evento.
export interface UpdateEventRequest {
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  latitude: number;
  longitude: number;
  address: string;
  localId: number | null;
}