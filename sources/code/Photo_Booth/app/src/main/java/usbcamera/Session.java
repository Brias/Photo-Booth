// Copyright 2000 by David Brownell <dbrownell@users.sourceforge.net>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

package usbcamera;


/**
 * Encapsulates the session between a PTP initiator and responder.
 *
 * @author David Brownell
 * @version $Id: Session.java,v 1.3 2001/04/12 23:13:00 dbrownell Exp $
 */

//Changed to public class in order to be accessible from outside the library
public class Session {
    private int sessionId;
    private int xid;
    private boolean active;
    private NameFactory factory;

    Session() {
    }

    void setFactory(NameFactory f) {
        factory = f;
    }

    NameFactory getFactory() {
        return factory;
    }

    /*
    Public to make it accessible from outside the package
     */
    public int getNextXID() {
        return (active ? xid++ : 0);
    }

    /*
    Public to make it accessible from outside the package
     */
    public int getNextSessionID() {
        if (!active)
            return ++sessionId;
        throw new IllegalStateException("already active");
    }

    /*
    Public to make it accessible from outside the package
     */
    public boolean isActive() {
        return active;
    }

    /*
    Public to make it accessible from outside the package
     */
    public void open() {
        xid = 1;
        active = true;
    }

    /*
    Public to make it accessible from outside the package
     */
    public void close() {
        active = false;
    }

    /*
    Public to make it accessible from outside the package
     */
    public int getSessionId() {
        return sessionId;
    }

    // track objects and their info by handles;
    // hookup to marshaling system and event framework
}
