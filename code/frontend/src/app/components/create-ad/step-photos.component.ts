import { Component, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-photos',
  standalone: true,
  templateUrl: './step-photos.component.html',
})
export class StepPhotosComponent {
  private route = inject(ActivatedRoute);
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

    if (typeof (this.facade as any).addImages === 'function') {
      (this.facade as any).addImages(files);
    } else {
      for (const f of files) this.facade.addImages(files);
    }

    this.rebuildPreviews();
    input.value = '';
  }

  remove(i: number) {
    this.facade.removeImage(i);
    this.rebuildPreviews();
  }

  next() {
    this.router.navigate(['../review'], { relativeTo: this.route });
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
