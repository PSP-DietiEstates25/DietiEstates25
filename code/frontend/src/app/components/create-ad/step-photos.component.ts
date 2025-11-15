import { Component, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-photos',
  standalone: true,
  templateUrl: './step-photos.component.html',
})
export class StepPhotosComponent {
  private activatedRoute = inject(ActivatedRoute);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);

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

    if (typeof (this.facade as any).addImages === 'function') {
      (this.facade as any).addImages(files);
    } else {
      for (const file of files) this.facade.addImages(files);
    }

    this.rebuildPreviews();
    input.value = '';
  }

  remove(index: number) {
    this.facade.removeImage(index);
    this.rebuildPreviews();
  }

  next() {
    this.routerService.navigate(['../review'], { relativeTo: this.activatedRoute });
  }

  private rebuildPreviews() {
    this.clearPreviews();
    this.previews = (this.facade.images() ?? []).map((file) =>
      URL.createObjectURL(file)
    );
  }

  private clearPreviews() {
    for (const url of this.previews) URL.revokeObjectURL(url);
    this.previews = [];
  }
}
