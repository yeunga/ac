/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2014.                            (c) 2014.
 *  Government of Canada                 Gouvernement du Canada
 *  National Research Council            Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 *  All rights reserved                  Tous droits réservés
 *
 *  NRC disclaims any warranties,        Le CNRC dénie toute garantie
 *  expressed, implied, or               énoncée, implicite ou légale,
 *  statutory, of any kind with          de quelque nature que ce
 *  respect to the software,             soit, concernant le logiciel,
 *  including without limitation         y compris sans restriction
 *  any warranty of merchantability      toute garantie de valeur
 *  or fitness for a particular          marchande ou de pertinence
 *  purpose. NRC shall not be            pour un usage particulier.
 *  liable in any event for any          Le CNRC ne pourra en aucun cas
 *  damages, whether direct or           être tenu responsable de tout
 *  indirect, special or general,        dommage, direct ou indirect,
 *  consequential or incidental,         particulier ou général,
 *  arising from the use of the          accessoire ou fortuit, résultant
 *  software.  Neither the name          de l'utilisation du logiciel. Ni
 *  of the National Research             le nom du Conseil National de
 *  Council of Canada nor the            Recherches du Canada ni les noms
 *  names of its contributors may        de ses  participants ne peuvent
 *  be used to endorse or promote        être utilisés pour approuver ou
 *  products derived from this           promouvoir les produits dérivés
 *  software without specific prior      de ce logiciel sans autorisation
 *  written permission.                  préalable et particulière
 *                                       par écrit.
 *
 *  This file is part of the             Ce fichier fait partie du projet
 *  OpenCADC project.                    OpenCADC.
 *
 *  OpenCADC is free software:           OpenCADC est un logiciel libre ;
 *  you can redistribute it and/or       vous pouvez le redistribuer ou le
 *  modify it under the terms of         modifier suivant les termes de
 *  the GNU Affero General Public        la “GNU Affero General Public
 *  License as published by the          License” telle que publiée
 *  Free Software Foundation,            par la Free Software Foundation
 *  either version 3 of the              : soit la version 3 de cette
 *  License, or (at your option)         licence, soit (à votre gré)
 *  any later version.                   toute version ultérieure.
 *
 *  OpenCADC is distributed in the       OpenCADC est distribué
 *  hope that it will be useful,         dans l’espoir qu’il vous
 *  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
 *  without even the implied             GARANTIE : sans même la garantie
 *  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
 *  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
 *  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
 *  General Public License for           Générale Publique GNU Affero
 *  more details.                        pour plus de détails.
 *
 *  You should have received             Vous devriez avoir reçu une
 *  a copy of the GNU Affero             copie de la Licence Générale
 *  General Public License along         Publique GNU Affero avec
 *  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
 *  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
 *                                       <http://www.gnu.org/licenses/>.
 *
 *  $Revision: 4 $
 *
 ************************************************************************
 */
package ca.nrc.cadc.ac;

public class PersonalDetails implements UserDetails
{
    /**
     * Name of the PersonalDetails element.
     */
    public static final String NAME = "personalDetails";
    
    /**
     * Name of the firstName element.
     */
    public static final String FIRSTNAME = "firstName";
    
    /**
     * Name of the lastName element.
     */
    public static final String LASTNAME = "lastName";
    
    /**
     * Name of the email element.
     */
    public static final String EMAIL = "email";
    
    /**
     * Name of the email element.
     */
    public static final String ADDRESS = "address";
    
    /**
     * Name of the email element.
     */
    public static final String INSTITUTE = "institute";
    
    /**
     * Name of the email element.
     */
    public static final String CITY = "city";
    
    /**
     * Name of the email element.
     */
    public static final String COUNTRY = "country";
        
    private String firstName;
    private String lastName;
    public String email;
    public String address;
    public String institute;
    public String city;
    public String country;

    public PersonalDetails(String firstName, String lastName)
    {
        if (firstName == null)
        {
            throw new IllegalArgumentException("null firstName");
        }
        if (lastName == null)
        {
            throw new IllegalArgumentException("null lastName");
        }

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    /* (non-Javadoc)
     * @see ca.nrc.cadc.auth.model.UserDetails#hashCode()
     */
    @Override
    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = prime * result + firstName.hashCode();
        result = prime * result + lastName.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof PersonalDetails))
        {
            return false;
        }
        PersonalDetails other = (PersonalDetails) obj;
        if (!firstName.equals(other.firstName))
        {
            return false;
        }
        return lastName.equals(other.lastName);
    }

    /* (non-Javadoc)
     * @see ca.nrc.cadc.auth.model.UserDetails#toString()
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + firstName + ", " + 
               lastName + ", " + email + ", " + address + ", " + 
               institute + ", " + city + ", " + country + "]";
    }

}
