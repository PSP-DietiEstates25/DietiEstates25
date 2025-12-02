import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffersPaginatorComponent } from './offers-paginator.component';

describe('OffersPaginatorComponent', () => {
  let component: OffersPaginatorComponent;
  let fixture: ComponentFixture<OffersPaginatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffersPaginatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OffersPaginatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
