#import "DarkMode.h"

/**
 * This plugin initializes Instabug.
 */
@implementation DarkMode

/**
 * Intializes Instabug and sets provided options.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) darkmode:(CDVInvokedUrlCommand*)command
{
    BOOL enabled = FALSE;
    if (@available(iOS 12, *))
    {
        if (self.viewController.traitCollection.userInterfaceStyle == UIUserInterfaceStyleDark)
        {
            enabled = TRUE;
        }
    }
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:enabled];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
