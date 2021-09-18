export const SECURITY_MODE_STANDALONE = 'cookie'
export const SECURITY_MODE_JWT = 'jwt'

const baseUrl = process.env.BASE_URL || '/'

let browserUrl = process.env.KRATOS_BROWSER_URL || ''
let publicUrl = process.env.KRATOS_PUBLIC_URL || ''


// Variable config has keys:
// kratos: {
//
//   // The browser config key is used to redirect the user. It reflects where ORY Kratos' Public API
//   // is accessible from. Here, we're assuming traffic going to `http://example.org/.ory/kratos/public/`
//   // will be forwarded to ORY Kratos' Public API.
//   browser: 'https://kratos.example.org',
//
//   // The location of the ORY Kratos Admin API
//   admin: 'https://ory-kratos-admin.example-org.vpc',
//
//   // The location of the ORY Kratos Public API within the cluster
//   public: 'https://ory-kratos-public.example-org.vpc',
// },

export default {
  kratos: {
    browser: browserUrl.replace(/\/+$/, ''),
    admin: (process.env.KRATOS_ADMIN_URL || '').replace(/\/+$/, ''),
    public: publicUrl.replace(/\/+$/, ''),
  },
  baseUrl,
  jwksUrl: process.env.JWKS_URL || '/',
  projectName: process.env.PROJECT_NAME || 'SecureApp',

  SECURITY_MODE_JWT,
  SECURITY_MODE_STANDALONE,

  https: {
    enabled:
      process.env.hasOwnProperty('TLS_KEY_PATH') &&
      process.env.hasOwnProperty('TLS_CERT_PATH'),
    certificatePath: process.env.TLS_CERT_PATH || '',
    keyPath: process.env.TLS_KEY_PATH || '',
  },
}
