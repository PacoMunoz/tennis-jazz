/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { PlayerTennisDetailComponent } from 'app/entities/player-tennis/player-tennis-detail.component';
import { PlayerTennis } from 'app/shared/model/player-tennis.model';

describe('Component Tests', () => {
  describe('PlayerTennis Management Detail Component', () => {
    let comp: PlayerTennisDetailComponent;
    let fixture: ComponentFixture<PlayerTennisDetailComponent>;
    const route = ({ data: of({ player: new PlayerTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [PlayerTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlayerTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.player).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
