package de.i3mainz.springframework.swe.n52.sos;

import org.n52.oxf.OXFException;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.util.SosUtil;

import de.i3mainz.springframework.ogc.OWSConnectionParameter;

public class SOSConnectionParameter extends OWSConnectionParameter {

    private Binding binding;

    public SOSConnectionParameter() {
        super();
        this.setService(SosUtil.SERVICE_TYPE);
    }

    /**
     * @return the binding
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * @param binding
     *            the binding to set
     * @throws OXFException
     */
    public void setBinding(Binding binding) throws OXFException {
        this.binding = binding;
    }

}
