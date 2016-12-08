const toPromise = (deferred) => {
  return new Promise((resolve, reject) => {
    deferred.then(resolve, reject);
  });
};

export default toPromise;
