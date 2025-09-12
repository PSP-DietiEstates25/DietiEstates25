import { Injectable, computed, signal } from '@angular/core';

export interface AdDraft {
  title: string;
  price: number | null;
  city: string;
  address: string;
  type: string;
  size: number | null;
  description: string;
  photos: File[];
}

const EMPTY: AdDraft = {
  title: '',
  price: null,
  city: '',
  address: '',
  type: 'Appartamento',
  size: null,
  description: '',
  photos: [],
};

@Injectable({ providedIn: 'root' })
export class AdDraftService {
  private KEY = 'de.ad.draft';
  draft = signal<AdDraft>(this.load());

  patch(p: Partial<AdDraft>) {
    this.draft.update((d) => ({ ...d, ...p }));
    this.save();
  }
  setPhotos(files: File[]) {
    this.patch({ photos: files });
  }
  reset() {
    this.draft.set(structuredClone(EMPTY));
    this.save();
  }

  basicsValid = computed(
    () =>
      !!this.draft().title &&
      !!this.draft().city &&
      (this.draft().price ?? -1) >= 0
  );
  detailsValid = computed(() => true); 
  photosValid = computed(() => true); 
  allValid = computed(() => this.basicsValid() && this.detailsValid());

  toFormData(): FormData {
    const d = this.draft();
    const fd = new FormData();
    fd.set('title', d.title);
    if (d.price != null) fd.set('price', String(d.price));
    fd.set('city', d.city);
    fd.set('address', d.address);
    fd.set('type', d.type);
    if (d.size != null) fd.set('size', String(d.size));
    fd.set('description', d.description);
    d.photos.forEach((f, i) =>
      fd.append('photos', f, f.name || `photo_${i}.jpg`)
    );
    return fd;
  }

  private save() {
    try {
      const obj = this.draft();
      const safe = { ...obj, photos: [] as any };
      sessionStorage.setItem(this.KEY, JSON.stringify(safe));
    } catch {}
  }
  private load(): AdDraft {
    try {
      const raw = sessionStorage.getItem(this.KEY);
      return raw
        ? { ...EMPTY, ...JSON.parse(raw), photos: [] }
        : structuredClone(EMPTY);
    } catch {
      return structuredClone(EMPTY);
    }
  }
}
