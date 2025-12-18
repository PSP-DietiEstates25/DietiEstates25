import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationPaginatorComponent } from './notification-paginator.component';

describe('NotificationPaginatorComponent', () => {
  let component: NotificationPaginatorComponent;
  let fixture: ComponentFixture<NotificationPaginatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationPaginatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationPaginatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
