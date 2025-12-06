import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentOffersListComponent } from './agent-offers-list.component';

describe('AgentOffersListComponent', () => {
  let component: AgentOffersListComponent;
  let fixture: ComponentFixture<AgentOffersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentOffersListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentOffersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
