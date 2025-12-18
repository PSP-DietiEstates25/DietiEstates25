export type NotificationCategory =
  | 'NEW_PROPERTIES'
  | 'PROMOTIONAL'
  | 'VISIT'
  | 'OFFER';

export interface NotificationPreferenceVM {
  category: NotificationCategory;
  enabled: boolean;
  id?: number;
}

export const ALL_NOTIFICATION_CATEGORIES: NotificationCategory[] = [
  'NEW_PROPERTIES',
  'PROMOTIONAL',
  'VISIT',
  'OFFER',
];

export function adaptUserPreferences(
  api: NotificationCategoryResponse[] | null | undefined,
): NotificationPreferenceVM[] {
  const byName = new Map<string, NotificationCategoryResponse>();

  for (const c of api ?? []) {
    if (!c.name) continue;
    byName.set(c.name.toUpperCase(), c);
  }

  return ALL_NOTIFICATION_CATEGORIES.map((name) => {
    const match = byName.get(name);
    return {
      category: name,
      enabled: Boolean(match?.isActive),
      id: match?.id,
    };
  });
}
