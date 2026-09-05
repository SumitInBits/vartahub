<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meeting Scheduled — VartaHub</title>
</head>

<body style="margin: 0; padding: 0; background-color: #f4f6f8; font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif; color: #172033;">

<table style="width: 100%; border-collapse: collapse; border-spacing: 0; background-color: #f4f6f8;">
    <tr>
        <td style="padding: 48px 16px; text-align: center;">
            <!-- Main Card -->
            <table style="width: 100%; max-width: 620px; margin: 0 auto; border-collapse: separate; border-spacing: 0; background-color: #ffffff; border-radius: 16px; overflow: hidden; text-align: left;">

                <!-- Header -->
                <tr>
                    <td style="padding: 30px 40px; border-bottom: 1px solid #edf0f3;">
                        <div style="font-size: 22px; line-height: 1.3; font-weight: 700; letter-spacing: -0.5px; color: #111827;">
                            VartaHub
                        </div>
                        <div style="margin-top: 4px; font-size: 12px; line-height: 1.4; color: #98a2b3;">
                            A product by XorGrid
                        </div>
                    </td>
                </tr>

                <!-- Content -->
                <tr>
                    <td style="padding: 42px 40px 40px;">

                        <!-- Eyebrow -->
                        <div style="margin-bottom: 12px; font-size: 12px; line-height: 1.4; font-weight: 700; letter-spacing: 1px; color: #667085; text-transform: uppercase;">
                            Meeting scheduled
                        </div>

                        <!-- Heading -->
                        <h1 style="margin: 0 0 14px; font-size: 30px; line-height: 1.25; font-weight: 700; letter-spacing: -0.8px; color: #111827;">
                            Your meeting is confirmed
                        </h1>

                        <p style="margin: 0 0 32px; font-size: 16px; line-height: 1.7; color: #667085;">
                            Your meeting has been successfully scheduled on VartaHub.
                            Here are the details you'll need.
                        </p>

                        <!-- Meeting Details -->
                        <table style="width: 100%; border-collapse: separate; border-spacing: 0; background-color: #f8fafc; border: 1px solid #edf0f3; border-radius: 12px;">

                            <!-- Topic -->
                            <tr>
                                <td style="padding: 24px 24px 18px;">
                                    <div style="margin-bottom: 7px; font-size: 12px; line-height: 1.4; font-weight: 600; letter-spacing: 0.3px; color: #8a94a6;">
                                        TOPIC
                                    </div>
                                    <div style="font-size: 17px; line-height: 1.5; font-weight: 600; color: #172033;">
                                        ${topic!"No Topic Specified"}
                                    </div>
                                </td>
                            </tr>

                            <!-- Date & Time -->
                            <tr>
                                <td style="padding: 6px 24px 18px;">
                                    <div style="margin-bottom: 7px; font-size: 12px; line-height: 1.4; font-weight: 600; letter-spacing: 0.3px; color: #8a94a6;">
                                        DATE &amp; TIME
                                    </div>
                                    <div style="font-size: 17px; line-height: 1.5; font-weight: 600; color: #172033;">
                                        ${scheduledAt!"TBD"}
                                    </div>
                                </td>
                            </tr>

                            <!-- Participants -->
                            <tr>
                                <td style="padding: 6px 24px 24px;">
                                    <div style="margin-bottom: 7px; font-size: 12px; line-height: 1.4; font-weight: 600; letter-spacing: 0.3px; color: #8a94a6;">
                                        PARTICIPANTS
                                    </div>
                                    <div style="font-size: 15px; line-height: 1.7; color: #475467;">
                                        <#if participantsFullName?? && participantsFullName?has_content>
                                            <#list participantsFullName as name>
                                                ${name!""}<#if name_has_next> · </#if>
                                            </#list>
                                        <#else>
                                            No participants
                                        </#if>
                                    </div>
                                </td>
                            </tr>

                        </table>

                        <p style="margin: 28px 0 24px; font-size: 15px; line-height: 1.7; color: #667085;">
                            Please be available at the scheduled time. You can view
                            the complete meeting details from your VartaHub dashboard.
                        </p>

                        <!-- CTA -->
                        <a href="${meetingURL!'#'}"
                           style="display: inline-block; padding: 13px 22px; background-color: #172033; border-radius: 9px; font-size: 14px; line-height: 1.4; font-weight: 600; color: #ffffff; text-decoration: none;">
                            View meeting
                        </a>

                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="padding: 24px 40px; border-top: 1px solid #edf0f3;">
                        <p style="margin: 0 0 6px; font-size: 12px; line-height: 1.6; color: #98a2b3;">
                            VartaHub is a product by XorGrid.
                        </p>
                        <p style="margin: 0; font-size: 12px; line-height: 1.6; color: #98a2b3;">
                            © ${.now?string('yyyy')} XorGrid. All rights reserved.
                        </p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

</body>
</html>