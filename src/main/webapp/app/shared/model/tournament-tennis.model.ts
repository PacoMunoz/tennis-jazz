import { Moment } from 'moment';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

export interface ITournamentTennis {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  inProgress?: boolean;
  winPoints?: number;
  lossPoints?: number;
  notPresentPoints?: number;
  injuredPoints?: number;
  groups?: ITournamentGroupTennis[];
}

export class TournamentTennis implements ITournamentTennis {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public inProgress?: boolean,
    public winPoints?: number,
    public lossPoints?: number,
    public notPresentPoints?: number,
    public injuredPoints?: number,
    public groups?: ITournamentGroupTennis[]
  ) {
    this.inProgress = this.inProgress || false;
  }
}
