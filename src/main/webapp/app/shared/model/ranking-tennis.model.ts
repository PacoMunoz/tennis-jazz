import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

export interface IRankingTennis {
  id?: number;
  points?: number;
  gamesWon?: number;
  gamesLoss?: number;
  setsWon?: number;
  setsLoss?: number;
  matchesPlayed?: number;
  matchesWon?: number;
  matchesLoss?: number;
  matchesNotPresent?: number;
  matchesAbandoned?: number;
  tournamentGroup?: ITournamentGroupTennis;
  player?: IPlayerTennis;
}

export class RankingTennis implements IRankingTennis {
  constructor(
    public id?: number,
    public points?: number,
    public gamesWon?: number,
    public gamesLoss?: number,
    public setsWon?: number,
    public setsLoss?: number,
    public matchesPlayed?: number,
    public matchesWon?: number,
    public matchesLoss?: number,
    public matchesNotPresent?: number,
    public matchesAbandoned?: number,
    public tournamentGroup?: ITournamentGroupTennis,
    public player?: IPlayerTennis
  ) {}
}
