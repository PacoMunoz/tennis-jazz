export interface IGenderTennis {
  id?: number;
  name?: string;
}

export class GenderTennis implements IGenderTennis {
  constructor(public id?: number, public name?: string) {}
}
