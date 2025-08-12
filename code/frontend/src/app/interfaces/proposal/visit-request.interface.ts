import { ProposalRequest } from "./proposal-request";

export interface VisitRequest extends ProposalRequest {
    dateTime: Date
}