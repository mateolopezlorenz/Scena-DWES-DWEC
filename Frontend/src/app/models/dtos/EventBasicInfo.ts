import { Local } from '../localModel';

//Información resumida de un evento.

export interface EventBasicInfo {
  id: number;
  name: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
}