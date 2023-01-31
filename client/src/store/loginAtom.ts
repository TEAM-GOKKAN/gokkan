import { atomsWithMutation } from 'jotai-tanstack-query';
import axios from 'axios';

const url = '';

const [postAtom] = atomsWithMutation((get) => ({
  mutationKey: ['posts'],
  mutationFn: async (code: string) => {
    const res = await axios({
      method: 'post',
      url,
      data: {
        code,
      },
      headers: {
        'Content-Type': `application/json`,
      },
    });
    return res.data;
  },
}));

export { postAtom };
