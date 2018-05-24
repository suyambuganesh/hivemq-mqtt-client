# Contributing Patches to The mqtt-bee Project

References for discussion:

* https://julien.ponge.org/blog/developer-certificate-of-origin-versus-contributor-license-agreements/
* https://meshedinsights.com/2016/01/07/apache-license-yes-apache-cla-no/


## Alternative 1: Using Contributor License Agreements (based on Apache CLAs)

Contributors must sign an [individual](individual-cla.txt) or [corporate](corporate-cla.txt) CLA before submitting
patches. Our CLAs are based are literally taken from the [Apache CLA](http://www.apache.org/licenses/) but "The Apache
Foundation" is replaced by "The mqtt-bee Project". The Apache Foundation explicitly [allows to re-use and modify](
http://www.apache.org/foundation/license-faq.html#CLA-Usage) their CLAs.

Signed CLAs must be scanned and send to the project via email (we need a project email address).

## Alternative 2: Repository License automatically applies also to contributions

Since the whole mqtt-bee repository is under Apache 2.0 and all source files must also contain an Apache 2.0 license
header, all contributions are implicitly licensed under Apache 2.0.

## Alternative 2.1: Sign your work -- the Developer's Certificate of Origin

By signing off commits (`git commit -s`) contributors must apply to the [Developer's Certificate of Origin 1.1](
https://developercertificate.org/). Probably we should also maintain a list of contributors containing their names and
email.
```
Developer Certificate of Origin
Version 1.1

Copyright (C) 2004, 2006 The Linux Foundation and its contributors.
1 Letterman Drive
Suite D4700
San Francisco, CA, 94129

Everyone is permitted to copy and distribute verbatim copies of this
license document, but changing it is not allowed.


Developer's Certificate of Origin 1.1

By making a contribution to this project, I certify that:

(a) The contribution was created in whole or in part by me and I
    have the right to submit it under the open source license
    indicated in the file; or

(b) The contribution is based upon previous work that, to the best
    of my knowledge, is covered under an appropriate open source
    license and I have the right under that license to submit that
    work with modifications, whether created in whole or in part
    by me, under the same open source license (unless I am
    permitted to submit under a different license), as indicated
    in the file; or

(c) The contribution was provided directly to me by some other
    person who certified (a), (b) or (c) and I have not modified
    it.

(d) I understand and agree that this project and the contribution
    are public and that a record of the contribution (including all
    personal information I submit with it, including my sign-off) is
    maintained indefinitely and may be redistributed consistent with
    this project or the open source license(s) involved.
```

# Remarks

* We should add [SPDX license identifiers](
  https://spdx.org/sites/cpstandard/files/pages/files/using_spdx_license_list_short_identifiers.pdf) to all files in the
  repository.