$ErrorActionPreference = 'Stop'
$fakeDiff = '+JWT_' + 'ACCESS_SECRET=synthetic-secret-that-must-be-blocked-1234567890'
$placeholderDiff = "+ADMIN_BOOTSTRAP_TOKEN=replace-with-a-local-bootstrap-secret"
$pattern = '(JWT_(ACCESS|REFRESH)_SECRET|PASSWORD|TOKEN)[=:][\s]*[A-Za-z0-9+/=_-]{20,}'
if ($fakeDiff -notmatch $pattern) { throw 'El patrón no detectó el secreto sintético.' }
if ($placeholderDiff -match $pattern -and $placeholderDiff -notmatch '(CHANGE_ME|replace-with-a-local)') { throw 'El placeholder fue clasificado incorrectamente.' }
Write-Output 'PASS: secreto sintético bloqueado; placeholder documentado permitido.'
