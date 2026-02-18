import { Local } from '../localModel';
import { UserResponse } from './UserResponse';



export interface EventResponse {
  id: number;
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
  rooms: number;
  createdAt: string;
  user: UserResponse;
  local: Local;
}