import { Request, Response } from 'express'
import { Configuration, V0alpha2Api } from '@ory/kratos-client'
import config from '../config'

// Uses the ORY Kratos NodeJS SDK:
const kratos = new V0alpha2Api(
  new Configuration({ basePath: config.kratos.public })
)

type UserRequest = Request & { user: any }

const authInfo = (req: UserRequest) => {
  const session = req.cookies.ory_kratos_session
  if (session) {
    return {
      raw: session,
      claims: req.user,
    }
  } else {
    return {

    }
  }

}

// A simple express handler that shows the dashboard screen.
export default async (req: Request, res: Response) => {
  const interestingHeaders = req.rawHeaders.reduce(
    (p: string[], v: string, i) =>
      i % 2 ? p : [...p, `${v}: ${req.rawHeaders[i + 1]}`],
    []
  )

  const ai = authInfo(req as UserRequest)

  // Create a logout URL
  const {
    data: { logout_url: logoutUrl },
  } = await kratos.createSelfServiceLogoutFlowUrlForBrowsers(
    req.header('Cookie')
  )

  res.render('dashboard', {
    session: ai.claims.session,
    token: ai,
    logoutUrl,
    headers: `GET ${req.path} HTTP/1.1

${interestingHeaders
  .filter((header: string) =>
    /User-Agent|Authorization|Content-Type|Host|Accept-Encoding|Accept-Language|Cookie|Connection|X-Forwarded-For/.test(
      header
    )
  )
  .join('\n')}
...`,
  })
}
