import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentVisitsListComponent } from './agent-visits-list.component';

describe('AgentVisitsListComponent', () => {
  let component: AgentVisitsListComponent;
  let fixture: ComponentFixture<AgentVisitsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentVisitsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentVisitsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
