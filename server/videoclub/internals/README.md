# internals

## sessions

Package used for user authentication. There is an instance of `SessionHandler` named
`SESSION_HANDLER` which maps session `tokens` to `users`. Tokens are added manually
and can be removed manually or automatically whenever they timeout.