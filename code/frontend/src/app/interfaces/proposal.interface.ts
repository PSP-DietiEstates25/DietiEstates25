import { Status } from "../enums/status.enum";

export interface Proposal {
    proposalId: number,
    status: Status;
}