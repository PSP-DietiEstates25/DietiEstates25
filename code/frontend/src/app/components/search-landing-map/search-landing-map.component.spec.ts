import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchLandingMapComponent } from './search-landing-map.component';

describe('SearchLandingMapComponent', () => {
  let component: SearchLandingMapComponent;
  let fixture: ComponentFixture<SearchLandingMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchLandingMapComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchLandingMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
