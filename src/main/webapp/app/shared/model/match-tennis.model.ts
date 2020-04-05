import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

export interface IMatchTennis {
  id?: number;
  localPlayerSet1Result?: number;
  visitorPlayerSet1Result?: number;
  localPlayerTBSet1Result?: number;
  visitorPlayerTBSet1Result?: number;
  localPlayerTBSet2Result?: number;
  visitorPlayerTBSet2Result?: number;
  localPlayerTBSet3Result?: number;
  visitorPlayerTBSet3Result?: number;
  localPlayerSet2Result?: number;
  visitorPlayerSet2Result?: number;
  localPlayerSet3Result?: number;
  visitorPlayerSet3Result?: number;
  localPlayerSets?: number;
  visitorPlayerSets?: number;
  localPlayerAbandoned?: boolean;
  visitorPlayerAbandoned?: boolean;
  localPlayerNotPresent?: boolean;
  visitorPlayerNotPresent?: boolean;
  postponed?: boolean;
  round?: IRoundTennis;
  visitorPlayer?: IPlayerTennis;
  localPlayer?: IPlayerTennis;
}

export class MatchTennis implements IMatchTennis {
  constructor(
    public id?: number,
    public localPlayerSet1Result?: number,
    public visitorPlayerSet1Result?: number,
    public localPlayerTBSet1Result?: number,
    public visitorPlayerTBSet1Result?: number,
    public localPlayerTBSet2Result?: number,
    public visitorPlayerTBSet2Result?: number,
    public localPlayerTBSet3Result?: number,
    public visitorPlayerTBSet3Result?: number,
    public localPlayerSet2Result?: number,
    public visitorPlayerSet2Result?: number,
    public localPlayerSet3Result?: number,
    public visitorPlayerSet3Result?: number,
    public localPlayerSets?: number,
    public visitorPlayerSets?: number,
    public localPlayerAbandoned?: boolean,
    public visitorPlayerAbandoned?: boolean,
    public localPlayerNotPresent?: boolean,
    public visitorPlayerNotPresent?: boolean,
    public postponed?: boolean,
    public round?: IRoundTennis,
    public visitorPlayer?: IPlayerTennis,
    public localPlayer?: IPlayerTennis
  ) {
    this.localPlayerAbandoned = this.localPlayerAbandoned || false;
    this.visitorPlayerAbandoned = this.visitorPlayerAbandoned || false;
    this.localPlayerNotPresent = this.localPlayerNotPresent || false;
    this.visitorPlayerNotPresent = this.visitorPlayerNotPresent || false;
    this.postponed = this.postponed || false;
  }
}
