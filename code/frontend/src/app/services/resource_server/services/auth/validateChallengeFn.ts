import { verifyChallenge } from "pkce-challenge";

export async function validateChallenge(code_challenge: string, code_verifier: string){
    return await verifyChallenge(code_verifier, code_challenge);
}