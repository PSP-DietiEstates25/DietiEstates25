import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdsPaginatorComponent } from './ads-paginator.component';

describe('AdsPaginatorComponent', () => {
  let component: AdsPaginatorComponent;
  let fixture: ComponentFixture<AdsPaginatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdsPaginatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdsPaginatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
