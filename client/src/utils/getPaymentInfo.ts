import {
  METHOD_FOR_CARD,
  QUOTAS,
  QUOTAS_FOR_INICIS_AND_KCP,
} from '../lib/constants/payment';

export function getMethods(pg: any) {
  switch (pg) {
    case 'kakaopay':
      return METHOD_FOR_CARD;
  }
}

export function getQuotas(pg: any, method: any) {
  if (method === 'card') {
    switch (pg) {
      case 'kcp':
        return { isQuotaRequired: true, quotas: QUOTAS_FOR_INICIS_AND_KCP };
      default:
        return { isQuotaRequired: true, quotas: QUOTAS };
    }
  }
}
