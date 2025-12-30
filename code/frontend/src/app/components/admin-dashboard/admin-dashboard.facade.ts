import { Injectable, inject, signal } from '@angular/core';
import { Observable, throwError, of, EMPTY, forkJoin } from 'rxjs';
import { map, catchError, switchMap, tap, finalize } from 'rxjs/operators';
import {
  RealEstateControllerService,
  EstateAgentControllerService,
  AdminControllerService,
  CadastralDataControllerService,
  DetailControllerService,
  GeographicalPositionControllerService,
  UtilityControllerService,
} from '../../services/services';

import { RealEstateResponse } from '../../services/models/real-estate-response';
import { AuthService } from '../../manual_services/auth/auth.service';
import { StafferRequest, StafferResponse } from '../../services/models';
import { PaginatorRequest } from '../../interfaces/paginator-request';
import { FullRealEstate } from '../../interfaces/full-real-estate';
import { AdCategory } from '../../enums/ad-category.enum';
import { EnergyClass } from '../../enums/energy-class.enum';
import { Role } from '../../interfaces/role';
import { AccountResponse } from '../../interfaces/account-response';
import { AccountRequest } from '../../interfaces/account-request';
import { AdminUser } from '../../interfaces/admin-user';

@Injectable({ providedIn: 'root' })
export class AdminDashboardFacade {
  realEstateService = inject(RealEstateControllerService);
  cadastralDataService = inject(CadastralDataControllerService);
  detailService = inject(DetailControllerService);
  geographicalPositionService = inject(GeographicalPositionControllerService);
  utilityService = inject(UtilityControllerService);
  private estateAgentService = inject(EstateAgentControllerService);
  private adminService = inject(AdminControllerService);
  private authService = inject(AuthService);

  // stato annunci
  realEstates = signal<FullRealEstate[]>([]);
  realEstatesLoading = signal(false);

  loading = signal(false);
  success = signal<string | null>(null);
  error = signal<string | null>(null);

  fetchRealEstates(request: PaginatorRequest) {
    const params = {
      page: request.page - 1,
      size: request.size,
    };
    return this.realEstateService.getRealEstates(params).pipe(
      switchMap((response) => {
        const requests = response.content!.map((realEstate) => {
          const cadastralData = this.cadastralDataService.getCadastralDataById({
            cadastraldataid: realEstate.cadastralDataId!,
          });

          const details = this.detailService
            .getDetailById({ detailid: realEstate.detailId! })
            .pipe(
              switchMap((detail) => {
                return forkJoin({
                  geographicalPosition:
                    this.geographicalPositionService.getGeographicalPositionById(
                      {
                        geographicalpositionid: detail.geographicalPositionId!,
                      },
                    ),
                  utility: this.utilityService.getUtilityById({
                    utilityid: detail.utilityId!,
                  }),
                });
              }),
            );

          return forkJoin({
            realEstate: of(realEstate),
            cadastralData: cadastralData,
            details: details,
          }).pipe(
            map((result) => {
              return {
                ...result.realEstate,
                cadastralData: result.cadastralData,
                geographicalPosition: result.details.geographicalPosition,
                utility: result.details.utility,
              };
            }),
          );
        });

        return forkJoin(requests).pipe(
          map((realEstatesObservables) => {
            const fullRealEstatesTyped: FullRealEstate[] =
              realEstatesObservables.map((realEstate) => ({
                ...realEstate,
                category: realEstate.category as unknown as AdCategory,
                cadastralData: {
                  ...realEstate.cadastralData,
                  energyClass: realEstate.cadastralData
                    .energyClass as unknown as EnergyClass,
                },
                geographicalPosition: realEstate.geographicalPosition,
                utility: realEstate.utility,
              }));

            return {
              ...response,
              fullRealEstates: fullRealEstatesTyped,
            };
          }),
        );
      }),
      tap((fullRealEstatesResponse) => {
        this.realEstates.set(fullRealEstatesResponse.fullRealEstates);
      }),
    );
  }

  deleteAd(adId: number): Observable<void> {
    const prev = this.realEstates();
    return this.realEstateService.deleteRealEstate({ realestateid: adId }).pipe(
      catchError((error) => {
        console.error('[Facade] deleteAd error (delete)', error);
        this.realEstates.set(prev);
        return of(void 0);
      }),
      map(() => void 0),
    );
  }

  updateAd(
    adId: number,
    patch: Partial<{ description: string }>,
  ): Observable<RealEstateResponse> {
    const body: any = { ...patch };
    return this.realEstateService.updateRealEstate({
      realestateid: adId,
      body,
    });
  }

  // crea un account nell'authorization server
  private authRegister(
    email: string,
    password: string,
    role?: Role,
  ) {
    const body: AccountRequest = { email, password, role };
    return this.authService.register(body);
  }

  // crea un account nel resource server
  createUser(body: {
    email: string;
    role: Role;
    password: string;
  }): Observable<AdminUser> {
    const { email, role, password } = body;

    switch (role) {
      case 'ESTATE_AGENT':
        return this.authRegister(email, password, 'ESTATE_AGENT').pipe(
          switchMap(() => {
            const payload: StafferRequest = { email };
            return this.estateAgentService.registerEstateAgent({
              body: payload,
            });
          }),
          map((agent: StafferResponse) => ({
            id: Number((agent as any)?.id) || Date.now(),
            email,
            role: 'ESTATE_AGENT' as const,
            active: true,
          })),
          catchError((error) => {
            console.error('[AdminDashboard] createUser(AGENT) error', error);
            return throwError(() => error);
          }),
        );

      case 'ADMIN':
        return this.authRegister(email, password, 'ADMIN').pipe(
          switchMap(() => {
            const payload: StafferRequest = { email };
            return this.adminService.registerAdmin({ body: payload });
          }),
          map(() => ({
            id: Date.now(),
            email,
            role: 'ADMIN' as const,
            active: true,
          })),
          catchError((e) => {
            console.error('[AdminDashboard] createUser(ADMIN) error', e);
            return throwError(() => e);
          }),
        );

      default:
        return throwError(
          () => new Error(`Ruolo non supportato: ${role as string}`),
        );
    }
  }

  changePassword(
    currentPassword: string,
    newPassword: string,
  ): Observable<void> {
    this.loading.set(true);
    this.success.set(null);
    this.error.set(null);

    return this.authService
      .changeAdminPassword({ oldPassword: currentPassword, newPassword })
      .pipe(
        tap(() => {
          this.success.set('Password aggiornata.');
          this.error.set(null);
        }),
        map(() => void 0),
        catchError((error) => {
          if (error?.status === 400) {
            this.error.set('La password corrente non è corretta.');
          } else if (error?.status === 401) {
            this.error.set('Sessione scaduta: accedi di nuovo.');
          } else if (error?.status === 403) {
            this.error.set('Non hai i permessi per questa operazione.');
          } else {
            this.error.set('Errore durante l’aggiornamento della password.');
          }
          return EMPTY;
        }),
        finalize(() => this.loading.set(false)),
      );
  }
}
