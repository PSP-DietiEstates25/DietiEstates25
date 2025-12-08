import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentAdsListComponent } from './agent-ads-list.component';

describe('AgentAdsListComponent', () => {
  let component: AgentAdsListComponent;
  let fixture: ComponentFixture<AgentAdsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentAdsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentAdsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
