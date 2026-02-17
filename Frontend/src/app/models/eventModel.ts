import { User } from './userModel';
import { Local } from './localModel';

export interface Events {
  id: number;
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
  rooms: number;
  user: User;
  local: Local;
}

