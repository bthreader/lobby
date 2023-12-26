## Matching Engine

A central registry to allow users to find, join, and leave lobbies.

The schema for the message types that the matching engine sends and receives is available in `../lobby-message-codecs`. To summarise:
 - **Join**
   - user specifies a lobby to join
 - **Leave**
   - user requests to leave the lobby they're in
 - **Match** (message id = 1)
   - user specifies a search criteria
   - if there is a hit, the lobby is joined
 - **Leave and Join**
   - user leaves the lobby they're in to immediately join another lobby

This approach could be horizontally scaled with respect to static logical groupings of lobbies. For example, if we assume lobby location is fixed, then we could one matching engine for the US, and one for Europe. This application however, runs as a single instance.
