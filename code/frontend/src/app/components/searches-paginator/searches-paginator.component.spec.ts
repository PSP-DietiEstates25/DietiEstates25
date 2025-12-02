import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchesPaginatorComponent } from './searches-paginator.component';

describe('SearchesPaginatorComponent', () => {
  let component: SearchesPaginatorComponent;
  let fixture: ComponentFixture<SearchesPaginatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchesPaginatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchesPaginatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
