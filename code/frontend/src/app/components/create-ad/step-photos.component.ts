import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-photos',
  standalone: true,
  templateUrl: './step-photos.component.html',
})
export class StepPhotosComponent {
  private facade = inject(CreateAdFacade);
  private router = inject(Router);

  previews: string[] = [];

  ngOnInit() {
    this.rebuildPreviews();
  }

  ngOnDestroy() {
    this.clearPreviews();
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);
    if (!files.length) return;
    this.facade.addImages(files); 
    this.rebuildPreviews(); 
    input.value = ''; 
  }

  remove(i: number) {
    this.facade.removeImage(i);
    this.rebuildPreviews();
  }

  next() {
    this.router.navigate(['/agent/ads/new/review']);
  }

  private rebuildPreviews() {
    this.clearPreviews();
    this.previews = (this.facade.images() ?? []).map((f) =>
      URL.createObjectURL(f)
    );
  }

  private clearPreviews() {
    for (const url of this.previews) URL.revokeObjectURL(url);
    this.previews = [];
  }
}
