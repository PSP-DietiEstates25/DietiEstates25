import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitsPaginatorComponent } from './visits-paginator.component';

describe('VisitsPaginatorComponent', () => {
  let component: VisitsPaginatorComponent;
  let fixture: ComponentFixture<VisitsPaginatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitsPaginatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisitsPaginatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
