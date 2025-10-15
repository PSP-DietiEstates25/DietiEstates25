import pkceChallenge from "pkce-challenge";
/**
 * ritorna un oggetto
 * {
 *      code_verifier: "verifier",
 *      code_challenge: "challenge"
 * }
 * 
 */
export async function generate32ByteChallenge(){
    return await pkceChallenge(32);
}