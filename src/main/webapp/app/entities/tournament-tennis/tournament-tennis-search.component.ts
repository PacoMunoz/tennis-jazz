import { Component, OnInit } from '@angular/core';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared';
import { ActivatedRoute } from '@angular/router';
import { TournamentTennisService } from 'app/entities/tournament-tennis/tournament-tennis.service';

@Component({
  selector: 'jhi-tournament-tennis-search',
  templateUrl: './tournament-tennis-search.component.html'
})
export class TournamentTennisSearchComponent implements OnInit {
  tournaments: ITournamentTennis[];
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;
  currentSearch: string;

  constructor(
    protected tournamentTennisService: TournamentTennisService,
    protected jhiAlertService: JhiAlertService,
    protected jhiParseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.tournaments = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }

  ngOnInit(): void {}
}
