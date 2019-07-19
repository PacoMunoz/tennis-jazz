import { Moment } from 'moment';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';

export interface IRoundTennis {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  tournamentGroup?: ITournamentGroupTennis;
  matchs?: IMatchTennis[];
}

export class RoundTennis implements IRoundTennis {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public tournamentGroup?: ITournamentGroupTennis,
    public matchs?: IMatchTennis[]
  ) {}
}
