xquery version "3.0";

declare namespace ext="http://www.altova.com/xslt-extensions";
declare namespace ns1 = "http://rail.dbschenker.de/MD/Codetable/Operations/V01";
declare namespace ns0 = "http://rail.dbschenker.de/MD/Codetable/Internal/Operations/V01";
declare namespace xf = "http://tempuri.org/MD_Codetable_V01/Transformation/CodetableRequest_TO_InternalCodetableRequest/";
declare namespace fn="http://www.w3.org/2005/xpath-functions";

   declare function xf:CodetableRequest_TO_InternalCodetableRequest($codetableRequest as element(ns1:CodetableRequest))
    as element(ns0:CodetableRequest) {
        let $CodetableRequest := $codetableRequest
        return
            <ns0:CodetableRequest
                xmlns:ns0="http://rail.dbschenker.de/MD/Codetable/Internal/Operations/V01"
            	xmlns:ns1="http://rail.dbschenker.de/MD/Codetable/Operations/V01">
            	{
                    for $Reference in $CodetableRequest/ns1:Reference
                    return
                        <ns0:Reference>{ data($Reference) }</ns0:Reference>
                }
				{
                    for $LowerModificationTimestamp in $CodetableRequest/ns1:LowerModificationTimestamp
                    return
                        <ns0:LowerModificationTimestamp>{concat((substring($LowerModificationTimestamp,1,10)),'-',
															(substring($LowerModificationTimestamp,12,2)),'.',
															(substring($LowerModificationTimestamp,15,2)),'.',
															(substring($LowerModificationTimestamp,18,2)),'.','00000')}</ns0:LowerModificationTimestamp>
                }
				 {
                    for $UpperModificationTimestamp in $CodetableRequest/ns1:UpperModificationTimestamp
                    return
                        <ns0:UpperModificationTimestamp>{concat((substring($UpperModificationTimestamp,1,10)),'-',
															(substring($UpperModificationTimestamp,12,2)),'.',
															(substring($UpperModificationTimestamp,15,2)),'.',
															(substring($UpperModificationTimestamp,18,2)),'.','00000')}</ns0:UpperModificationTimestamp>
                }
                 {
                    for $CodeartFilter in $CodetableRequest/ns1:CodeartFilter
                    return
                        <ns0:CodeartFilter>
                            {
                                for $Codeart in $CodeartFilter/ns1:Codeart
                                return
                                    <ns0:Codeart>{ data($Codeart) }</ns0:Codeart>
                            }
                        </ns0:CodeartFilter>
                }
            </ns0:CodetableRequest>
};         



declare variable $in.body as xs:string external;
let $codetableRequest :=parse-xml($in.body)/*
return xf:CodetableRequest_TO_InternalCodetableRequest($codetableRequest)

